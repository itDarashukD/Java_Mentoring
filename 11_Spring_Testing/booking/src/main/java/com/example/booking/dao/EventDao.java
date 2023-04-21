package com.example.booking.dao;

import com.example.booking.model.Event;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface EventDao {

    @Select("SELECT * FROM public.\"Event\" WHERE id = #{eventId}")
    Event getEventById(@Param("eventId") long eventId);

    @Select("SELECT * FROM public.\"Event\" WHERE title = #{title}")
    List<Event> getEventsByTitle(@Param("title") String title);

    @Select("SELECT * FROM public.\"Event\" WHERE date = #{date}")
    List<Event> getEventsForDay(@Param("date") String date);

    @Insert("INSERT INTO public.\"Event\" (title, date) VALUES (#{title},#{date})")
    void createEvent(Event event);

    @Update("Update public.\"Event\" set title= #{title}, date= #{date} where id=#{id}")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void updateEvent(Event event);

    @Delete("Delete from public.\"Event\" where id=#{public.\"Event\"Id}")
    void deleteEvent(long eventId);

}
