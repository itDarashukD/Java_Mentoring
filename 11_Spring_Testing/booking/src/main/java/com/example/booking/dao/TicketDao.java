package com.example.booking.dao;


import com.example.booking.model.Category;
import com.example.booking.model.Event;
import com.example.booking.model.Ticket;
import com.example.booking.model.User;
import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface TicketDao {

    @Insert("INSERT INTO public.\"Ticket\" (event_id, user_id, place, category) VALUES (#{eventId}, #{userId}, #{place}, #{category})")
    void bookTicket(Ticket ticket);

    @Result(column = "event_id", property = "eventId")
    @Result(column = "user_id", property = "userId")
    @Select("SELECT * FROM public.\"Ticket\" WHERE id = #{ticketId}")
    Ticket getById(@Param("ticketId") long ticketId);

    @Result(column = "event_id", property = "eventId")
    @Result(column = "user_id", property = "userId")
    @Select("SELECT * FROM public.\"Ticket\" WHERE event_id = #{eventId}")
    List<Ticket> getBookedTicketsByEvent(long eventId);

    @Result(column = "event_id", property = "eventId")
    @Result(column = "user_id", property = "userId")
    @Select("SELECT * FROM public.\"Ticket\" WHERE user_id = #{userId}")
    List<Ticket> getBookedTicketsByUser(long userId);


    @Update("Update public.\"Ticket\" set event_id = #{eventId} , user_id = #{userId} , place = #{place} , category = #{category} where id=#{id}")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void update(Ticket ticketToRemoveUser);

}
