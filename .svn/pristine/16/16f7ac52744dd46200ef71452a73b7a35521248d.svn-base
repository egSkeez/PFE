/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xtensus.dao.utils;

import java.util.HashMap;
import java.util.Map;

public class QueryParameterBuilder {

    private Map<String, Object> parameters = null;

    public QueryParameterBuilder(String name, Object value) {
        parameters = new HashMap<String, Object>();
        parameters.put(name, value);
    }

    public static QueryParameterBuilder with(String name, Object value) {
       return new QueryParameterBuilder(name, value);
    }

    public QueryParameterBuilder and(String name, Object value) {
        parameters.put(name, value);
        return this;
    }

    public Map<String, Object> parameters() {
        return parameters;
    }
    
    
    
    
    
    
}
