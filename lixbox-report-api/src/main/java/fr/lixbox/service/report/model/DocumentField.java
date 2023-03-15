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
 * Cette entité représente un champ de fusion dans le document.
 *
 * @author ludovic.terral
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class DocumentField implements Serializable
{
    // ----------- Attribut -----------    
    private static final long serialVersionUID = 202203151459L;
    
    private String key;
    private Object value;



    // ----------- Methode -----------
    public DocumentField() 
    {
        super();
    }
    public DocumentField(String key, Object value) 
    {
        super();
        this.key=key;
        this.value=value;
    }
    
    
    
    public String getKey()
    {
        return key;
    }
    public void setKey(String key)
    {
        this.key = key;
    }


    
    public Object getValue()
    {
        return value;
    }
    public void setValue(Object value)
    {
        this.value = value;
    }



    @Override
    public String toString()
    {
        return JsonUtil.transformObjectToJson(this, false);
    }

    
    
    public static DocumentField valueOf(String json)
    {
        return JsonUtil.transformJsonToObject(json, new TypeReference<DocumentField>() {});
    }
}