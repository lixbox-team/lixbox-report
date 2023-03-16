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
 *   You should have received a copy of the GNU General Public License
 *    along with lixbox-report.  If not, see <https://www.gnu.org/licenses/>
 *   
 *   @AUTHOR Lixbox-team
 *
 ******************************************************************************/
package fr.lixbox.service.report;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.client.ClientBuilder;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import fr.lixbox.common.exceptions.BusinessException;
import fr.lixbox.service.report.model.Constant;
import fr.lixbox.service.report.model.Document;
import fr.lixbox.service.report.model.DocumentField;
import fr.lixbox.service.report.model.Langue;
import fr.lixbox.service.report.service.ReportServiceBean;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class TestReportService implements Serializable
{
    // ----------- Attribut(s) -----------
    private static final long serialVersionUID = 202303152010L;
    
    @Inject ReportServiceBean reportService;


    
    // ----------- Methode(s) -----------
    @BeforeAll
    public static void tearOf() throws IOException
    {
        if (new File("./test_l.docx").exists())
        {
            FileUtils.forceDelete(new File("./test_l.docx"));
        }
        if (new File("./test_r.docx").exists())
        {
            FileUtils.forceDelete(new File("./test_r.docx"));
        }
        if (new File("./test_l.pdf").exists())
        {
            FileUtils.forceDelete(new File("./test_l.pdf"));
        }
        if (new File("./test_r.pdf").exists())
        {
            FileUtils.forceDelete(new File("./test_r.pdf"));
        }
    }
    
    
    
    @Test
    void testGenerateDocx_local() throws IOException
    {        
        try
        (   
            InputStream in = ReportServiceBean.class.getResourceAsStream("/template/B1_FAL1_template.docx");
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
            Document report = reportService.generateDocument(Langue.FR_FR, template, fields, Constant.DOCX_MIME_TYPE);
            FileUtils.writeByteArrayToFile(new File("./test_l.docx"), report.getContent());
            Assertions.assertEquals(29860, report.getContent().length, "La taille du rendu est incorrecte. attendue: 29860 bytes, obtenue: "+report.getContent().length+" bytes");
        }
        catch (BusinessException e)
        {
            Assertions.fail(e.getMessage());
        }     
    }
    
    
    
    @Test
    void testGenerateDocx_remote() 
    {
        try
        (   
            InputStream in = ReportServiceBean.class.getResourceAsStream("/template/B1_FAL1_template.docx");
        )
        {   
            ResteasyClient client = (ResteasyClient)ClientBuilder.newClient();
            ResteasyWebTarget target = client.target("http://localhost:19110/report/api");
            ReportService reportService = target.proxy(ReportService.class);
            
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
            Document report = reportService.generateDocument(Langue.FR_FR, template, fields, Constant.DOCX_MIME_TYPE);
            FileUtils.writeByteArrayToFile(new File("./test_r.docx"), report.getContent());
            Assertions.assertEquals(29860, report.getContent().length, "La taille du rendu est incorrecte. attendue: 29860 bytes, obtenue: "+report.getContent().length+" bytes");
        }
        catch (Exception e)
        {
            Assertions.fail(e.getMessage());
        }        
    }
    
    
    
    @Test
    void testGeneratePdf_local() throws IOException
    {        
        try
        (   
            InputStream in = ReportServiceBean.class.getResourceAsStream("/template/B1_FAL1_template.docx");
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
            Document report = reportService.generateDocument(Langue.FR_FR, template, fields, Constant.PDF_MIME_TYPE);
            FileUtils.writeByteArrayToFile(new File("./test_l.pdf"), report.getContent());
            Assertions.assertEquals(65608, report.getContent().length, "La taille du rendu est incorrecte. attendue: 65608 bytes, obtenue: "+report.getContent().length+" bytes");
        }
        catch (BusinessException e)
        {
            Assertions.fail(e.getMessage());
        }     
    }
    
    
    
    @Test
    void testGeneratePdf_remote() 
    {
        try
        (   
            InputStream in = ReportServiceBean.class.getResourceAsStream("/template/B1_FAL1_template.docx");
        )
        {   
            ResteasyClient client = (ResteasyClient)ClientBuilder.newClient();
            ResteasyWebTarget target = client.target("http://localhost:19110/report/api");
            ReportService reportService = target.proxy(ReportService.class);
            
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
            Document report = reportService.generateDocument(Langue.FR_FR, template, fields, Constant.PDF_MIME_TYPE);
            FileUtils.writeByteArrayToFile(new File("./test_r.pdf"), report.getContent());
            Assertions.assertEquals(65608, report.getContent().length, "La taille du rendu est incorrecte. attendue: 65608 bytes, obtenue: "+report.getContent().length+" bytes");
        }
        catch (Exception e)
        {
            Assertions.fail(e.getMessage());
        }        
    }
}