package co.payrail.attendance_srv.auth.dto.utility;

import com.fasterxml.jackson.databind.JsonSerializer;

public interface PrettySerializer
{
    public  <T> JsonSerializer<T> getSerializer();
}
