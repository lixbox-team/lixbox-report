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
package fr.lixbox.service.report.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.type.TypeReference;

import fr.lixbox.io.json.JsonUtil;

/**
 * Cette entité représente le résultat de la génération.
 *
 * @author ludovic.terral
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class Document implements Serializable
{
    // ----------- Attribut -----------    
    private static final long serialVersionUID = 202203151124L;
    
    private String mimeType;
    private byte[] content;



    // ----------- Methode -----------
    public String getMimeType()
    {
        return mimeType;
    }
    public void setMimeType(String mimeType)
    {
        this.mimeType = mimeType;
    }



    public byte[] getContent()
    {
        return content;
    }
    public void setContent(byte[] content)
    {
        this.content = content;
    }
    
    
    
    @Override
    public String toString()
    {
        return JsonUtil.transformObjectToJson(this, false);
    }



    public static Document valueOf(String json)
    {
        return JsonUtil.transformJsonToObject(json, new TypeReference<Document>() {});
    }
}