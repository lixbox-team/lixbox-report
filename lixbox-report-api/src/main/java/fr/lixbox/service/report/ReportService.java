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
package fr.lixbox.service.report;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.eclipse.microprofile.health.HealthCheckResponse;

import fr.lixbox.common.exceptions.BusinessException;
import fr.lixbox.service.report.model.Document;
import fr.lixbox.service.report.model.DocumentField;
import fr.lixbox.service.report.model.Langue;

/**
 * Cette interface represente le contrat pour la génération de rapport.
 *
 * @author ludovic.terral
 */
@Path(ReportService.SERVICE_URI)
@Produces({"application/json", "application/x-www-form-urlencoded"})
@Consumes({"application/json", "application/x-www-form-urlencoded"})
public interface ReportService
{
	// ----------- Attribut -----------
	static final long serialVersionUID = 201705120951L;

    static final String SERVICE_NAME = "global-service-api-report";
    static final String SERVICE_CODE = "REPORTSERV";
    static final String SERVICE_VERSION = "1.0";
    static final String SERVICE_URI = SERVICE_VERSION;
    static final String FULL_SERVICE_URI = "/report/api/"+SERVICE_URI;



	// ----------- Methode -----------    

    @GET @Path("/health") HealthCheckResponse checkHealth();
    @GET @Path("/health/live") HealthCheckResponse checkLive();
    @GET @Path("/health/ready") HealthCheckResponse checkReady();
    @GET @Path("/version") String getVersion();
    
    @POST @Path("/{langue}/generate") Document generateDocument(@PathParam("langue") Langue langue, @FormParam("template") Document template, @FormParam("fields") List<DocumentField> fields, @QueryParam("outDocType") String typeDocSortie) throws BusinessException;
}