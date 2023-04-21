package com.example.event.dao;


import com.example.event.model.Event;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface EventDao {

    @Result(column = "event_id", property = "eventId")
    @Result(column = "event_type", property = "eventType")
    @Result(column = "date_time", property = "dateTime")
    @Select("SELECT * FROM public.\"Event\" ")
    List<Event> getAllEvents();

    @Result(column = "event_id", property = "eventId")
    @Result(column = "event_type", property = "eventType")
    @Result(column = "date_time", property = "dateTime")
    @Select("SELECT * FROM public.\"Event\" WHERE event_id = #{eventId}")
    Event getEventById(@Param("eventId") long eventId);

    @Insert("INSERT INTO public.\"Event\" (event_id, title, place, speaker, event_type, date_time ) VALUES (#{eventId},#{title},#{place},#{speaker},#{eventType},#{dateTime})")
    int createEvent(Event event);

    @Update("Update public.\"Event\" set title=#{title}, place=#{place}, speaker=#{speaker}, event_type=#{eventType}, date_time=#{dateTime} where event_id=#{eventId}")
    int updateEvent(Event event);

    @Delete("Delete from public.\"Event\" where event_id=#{eventId}")
    int deleteEvent(long eventId);

}
