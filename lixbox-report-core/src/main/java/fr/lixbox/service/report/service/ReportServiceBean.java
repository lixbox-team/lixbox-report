/*******************************************************************************
 *    
 *                           FRAMEWORK Lixbox
 *                          ==================
 *      
 *    This file is part of lixbox-report.
 *
 *    lixbox-report is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *    (at your option) any later version.
 *
 *    lixbox-report is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with lixbox-report.  If not, see <https://www.gnu.org/licenses/>
 *   
 *   @AUTHOR Lixbox-team
 *
 ******************************************************************************/
package fr.lixbox.service.report.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.security.PermitAll;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.commons.io.IOUtils;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.HealthCheckResponseBuilder;
import org.eclipse.microprofile.metrics.MetricUnits;
import org.eclipse.microprofile.metrics.annotation.Timed;

import fr.lixbox.common.exceptions.BusinessException;
import fr.lixbox.common.exceptions.ProcessusException;
import fr.lixbox.common.resource.LixboxResources;
import fr.lixbox.common.util.CodeVersionUtil;
import fr.lixbox.common.util.ExceptionUtil;
import fr.lixbox.io.document.util.ReportUtil;
import fr.lixbox.service.registry.RegistryService;
import fr.lixbox.service.registry.cdi.LocalRegistryConfig;
import fr.lixbox.service.report.LixboxReportResources;
import fr.lixbox.service.report.ReportService;
import fr.lixbox.service.report.model.Constant;
import fr.lixbox.service.report.model.Document;
import fr.lixbox.service.report.model.DocumentField;
import fr.lixbox.service.report.model.Langue;

/**
 * Cette classe est l'implémentation de l'API Report
 * 
 * @author ludovic.terral
 */
@ApplicationScoped
public class ReportServiceBean implements ReportService, Serializable
{
    // ----------- Attribut(s) -----------   
    private static final long serialVersionUID = 202203151146L;
    private static final String MSG_ERROR_EXCEPUTI_02 = "MSG.ERROR.EXCEPUTI_02";
    @Inject @LocalRegistryConfig RegistryService registryService;



    // ----------- Methode -----------
    @Override
    @PermitAll
    public HealthCheckResponse checkHealth() 
    {
        return checkReady();
    }

    
    
    @Override
    @PermitAll
    public HealthCheckResponse checkReady()
    {
        return HealthCheckResponse.named("ReportService Ready").up().build();
    }
    
    

    @Override 
    @PermitAll
    public HealthCheckResponse checkLive() 
    {
        HealthCheckResponseBuilder responseBuilder = HealthCheckResponse.named("ReportService Live");
        boolean up = true;
        
        // verif report docx to docx
        try
        (   
            InputStream in = ReportServiceBean.class.getResourceAsStream("/health/template/B1_FAL1_template.docx");
        )
        {   
            Document template = new Document();
            template.setMimeType(Constant.DOCX_MIME_TYPE);
            template.setContent(IOUtils.toByteArray(in));
            
            List<DocumentField> fields = new ArrayList<>();
            fields.add(new DocumentField("folder", "test"));
            fields.add(new DocumentField("XA","X"));
            fields.add(new DocumentField("XD", ""));
            fields.add(new DocumentField("name_type", "mon navire"));
            fields.add(new DocumentField("imo_number", "IMO1234586"));
            fields.add(new DocumentField("call_sign", "123CALL"));
            fields.add(new DocumentField("voyage_number", "VN_1234"));
            fields.add(new DocumentField("port_call", "Arrival : FRMRS"));
            fields.add(new DocumentField("flag_state", "FR"));
            fields.add(new DocumentField("date_eta", "12/12/2018 12:30"));
            fields.add(new DocumentField("date_etd", "12/12/2018 12:30"));            
            
            
            //verif report docx to docx
            Document report = generateDocument(Langue.FR_FR, template, fields, Constant.DOCX_MIME_TYPE);
            if (29860!=report.getContent().length)
            {
                up=false;
                String msg = "La taille du report en docx est incorrecte. attendue: 29860 bytes, obtenue: "+report.getContent().length+" bytes";
                responseBuilder.withData("generateReportDocxToDocx", msg);
            }
            else
            {
                responseBuilder.withData("generateReportDocxToDocx", "La fonction est opérationnelle");
            }

            
            //verif report docx to pdf
            report = generateDocument(Langue.FR_FR, template, fields, Constant.PDF_MIME_TYPE);
            if (65608!=report.getContent().length)
            {
                up=false;
                String msg = "La taille du report en pdf est incorrecte. attendue: 65608 bytes, obtenue: "+report.getContent().length+" bytes";
                responseBuilder.withData("generateReportDocxToPdf", msg);
            }
            else
            {
                responseBuilder.withData("generateReportDocxToPdf", "La fonction est opérationnelle");
            }
        } 
        catch (Exception e) 
        {
            // cannot access the database
            responseBuilder.down();
        }
        if (up) 
        {
            responseBuilder.up();
        }
        else 
        {
            responseBuilder.down();
        }
        return responseBuilder.build();
    }



    /**
     * Cette methode renvoie la version courante du code. 
     */
    @Override
    @PermitAll
    public String getVersion()
    {   
        return CodeVersionUtil.getVersion(this.getClass());
    }

    
    
    @Override
    @Timed(name = "TimeOfGenerateDocument", description = "A measure of how long it takes to perform the generateDocument.", unit = MetricUnits.MILLISECONDS)
    public Document generateDocument(Langue langue, Document template, List<DocumentField> fields, String typeDocSortie) 
        throws BusinessException
    {
        //Controler les parametres
        if (typeDocSortie==null)
        {
            throw new BusinessException(LixboxResources.getString(
                    MSG_ERROR_EXCEPUTI_02, 
                    new String[] { ReportService.SERVICE_CODE, "typeDocSortie" }));   
        }
        if (template==null)
        {
            throw new BusinessException(LixboxResources.getString(
                    MSG_ERROR_EXCEPUTI_02, 
                    new String[] { ReportService.SERVICE_CODE, "template" }));   
        }
        if (fields==null)
        {
            throw new BusinessException(LixboxResources.getString(
                    MSG_ERROR_EXCEPUTI_02, 
                    new String[] { ReportService.SERVICE_CODE, "fields" }));   
        }
        
        Document report=null;
        byte[] content = new byte[0];
                
        try
        {   
            switch (template.getMimeType())
            {
                case Constant.DOCX_MIME_TYPE:
                    content = generateDocxReport(langue, fields, template, typeDocSortie);                            
                    break;
                default:
                    throw new ProcessusException(LixboxReportResources.getString("template.format.unsupported", langue));
            }
            report = new Document();
            report.setContent(content);
            report.setMimeType(typeDocSortie);
        }    
        catch(Exception e) 
        {
            ExceptionUtil.traiterException(e, ReportService.SERVICE_CODE, true);
        }
        return report;
    }



    private byte[] generateDocxReport(Langue langue, List<DocumentField> fields, Document template, String typeDocSortie) 
        throws BusinessException
    {
        byte[] content = new byte[0];
        try
        (   
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ByteArrayInputStream in = new ByteArrayInputStream(template.getContent());
        )
        {   
            ReportUtil reportUtil = new ReportUtil(in, Calendar.getInstance().getTimeInMillis()+"");
            
            Map<String, Object> hFields = new HashMap<>();
            for (DocumentField field : fields)
            {
                hFields.put(field.getKey(), field.getValue());
            }
            switch(typeDocSortie) {
                case Constant.DOCX_MIME_TYPE:
                    reportUtil.generateReportDocxToDocx(out, hFields, reportUtil.getFieldsMetadata());
                    break;
                case Constant.PDF_MIME_TYPE:
                    reportUtil.generateReportDocxToPdf(out, hFields, reportUtil.getFieldsMetadata());
                    break;
                default:
                    throw new ProcessusException(LixboxReportResources.getString("out.format.unsupported", langue));
            }            
            content = out.toByteArray();
        }    
        catch(Exception e) 
        {
            ExceptionUtil.traiterException(e, ReportService.SERVICE_CODE, true);
        }      
        return content;
    }
}