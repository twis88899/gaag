package com.twis.common;

import java.io.IOException;
import java.text.SimpleDateFormat;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.SerializerProvider;

public class ObjectMapperEx extends ObjectMapper {

	private static final long serialVersionUID = -2684671684921694113L;

	public ObjectMapperEx() {
     	
//     	SimpleModule module = new SimpleModule();  
//     	module.addSerializer(String.class, new StringUnicodeSerializer());  
//     	this.registerModule(module);  

		this.configure(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS,false);
		this.getSerializerProvider().setNullValueSerializer(new JsonSerializerEx());
		setDateFormat((new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")));
	}

	
}


class JsonSerializerEx extends JsonSerializer<Object>{
	

	@Override
	public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider)
			throws IOException, JsonProcessingException {
		jgen.writeString("");
		
	}
}



