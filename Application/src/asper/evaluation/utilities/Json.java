package asper.evaluation.utilities;

import com.google.gson.*;

import java.lang.reflect.Type;

public class Json<T>
{
    static Gson gson;

    static
    {
        gson = new GsonBuilder()
                .registerTypeAdapter(Class.class, new Adapters.ClassAdapter())
                .disableHtmlEscaping()
                .setPrettyPrinting()
                .create();
    }

    public static String toJson(Object object)
    {
        return gson.toJson(object);
    }

    public static <T> T fromJson(String data, Class<T> type)
    {
        return gson.fromJson(data, type);
    }

    public static Gson get()
    {
        return gson;
    }

    static class Adapters
    {
        static class ClassAdapter implements JsonSerializer<Class<?>>, JsonDeserializer<Class<?>>
        {

            @Override
            public JsonElement serialize(Class<?> src, Type typeOfSrc, JsonSerializationContext context)
            {
                return new JsonPrimitive(src.getName());
            }

            @Override
            public Class<?> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                    throws JsonParseException
            {
                try
                {
                    return Class.forName(json.getAsString());
                } catch (ClassNotFoundException e)
                {
                    throw new RuntimeException(e);
                }
            }
        }
    }

}
