package com.tao.mapper;

import com.tao.pojo.Records;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface RecordsMapper {

    // 🔹 插入记录（id 是自增的）
    @Insert("INSERT INTO T_POINT_FORECAST_RECORD ( " +
            " AREACODE, MAKETIME,wordPath,isTeamWork,team,taskStatus,userId,dataType,taskName,productType,email,ftp) " +
            "VALUES (#{AREACODE}, #{makeTime},#{wordPath},#{isTeamWork},#{team},#{taskStatus},#{userId},#{dataType},#{taskName},#{productType},#{email},#{ftp})")
    @Options(useGeneratedKeys = true, keyProperty = "id") // 获取数据库生成的 id
    void insertRecord(Records record);


    @Select("select * from T_POINT_FORECAST_RECORD where taskName=#{taskName}")
    List<Records> check(String taskName);
    // 🔹 删除记录（根据 id）
    @Delete("DELETE FROM T_POINT_FORECAST_RECORD WHERE " +
            "ID=#{id}")
    void deleteRecord(Integer id);

    @Update("<script>" +
            "UPDATE T_POINT_FORECAST_RECORD SET" +
            "<trim suffixOverrides=','>" +
            "<if test='areaCode != null'>AREACODE = #{areaCode},</if>" +
            "<if test='makeTime != null'>MAKETIME = #{makeTime},</if>" +
            "<if test='wordPath != null'>wordPath = #{wordPath},</if>" +
            "<if test='isTeamWork != null'>isTeamWork = #{isTeamWork},</if>" +
            "<if test='team != null'>team = #{team},</if>" +
            "<if test='taskStatus != null'>taskStatus = #{taskStatus},</if>" +
            "<if test='userId != null'>userId = #{userId},</if>" +
            "<if test='taskName != null'>taskName = #{taskName},</if>" +
            "<if test='productType != null'>productType = #{productType},</if>" +
            "<if test='email != null'>email = #{email},</if>" +
            "<if test='ftp != null'>ftp = #{ftp},</if>" +
            "<if test='dataType != null'>dataType = #{dataType}</if>" +
            "</trim>" +
            "WHERE ID=#{id}" +
            "</script>")
    void updateRecord(Records record);


    @Select("select taskStatus from T_POINT_FORECAST_RECORD where ID=#{id}")
    Integer getTaskStatus(Integer id);

    @Select({
            "<script>",
            "SELECT * FROM T_POINT_FORECAST_RECORD",
            "WHERE 1=1",

            // 🔹 任务名称模糊查询
            "<if test='taskName != null and taskName != \"\"'>",
            "  AND taskName LIKE #{taskName}",
            "</if>",

            // 🔹 时间条件
            "<if test='startTime != null and endTime != null'>",
            "  AND makeTime BETWEEN #{startTime} AND #{endTime}",
            "</if>",
            "<if test='startTime != null and endTime == null'>",
            "  AND makeTime &gt;= #{startTime}",
            "</if>",
            "<if test='startTime == null and endTime != null'>",
            "  AND makeTime &lt;= #{endTime}",
            "</if>",

            // 🔹 产品类型条件
            "<if test='productType != null and productType != \"\"'>",
            "  AND productType = #{productType}",
            "</if>",

            // 🔹 areaCode 不为空时才启用过滤逻辑
            "<if test='areaCode != null and areaCode != \"\"'>",
            "  AND (",
            "      (taskStatus = 0 AND AREACODE = #{areaCode})",
            "   OR (taskStatus IN (1, 2) AND (AREACODE = #{areaCode} OR team LIKE #{teamLike}))",
            "  )",
            "</if>",

            "ORDER BY makeTime DESC",
            "LIMIT #{offset}, #{pageSize}",
            "</script>"
    })
    List<Records> getAllRecords(
            @Param("taskName") String taskName,
            @Param("startTime") String startTime,
            @Param("endTime") String endTime,
            @Param("productType") String productType,
            @Param("offset") int offset,
            @Param("pageSize") int pageSize,
            @Param("areaCode") String areaCode,
            @Param("teamLike") String teamLike
    );



    // 🔹 查询总条数
    @Select({
            "<script>",
            "SELECT COUNT(*) FROM T_POINT_FORECAST_RECORD",
            "WHERE 1=1",

            // 🔹 任务名称模糊查询
            "<if test='taskName != null and taskName != \"\"'>",
            "  AND taskName LIKE #{taskName}",
            "</if>",

            // 🔹 时间条件
            "<if test='startTime != null and endTime != null'>",
            "  AND makeTime BETWEEN #{startTime} AND #{endTime}",
            "</if>",
            "<if test='startTime != null and endTime == null'>",
            "  AND makeTime &gt;= #{startTime}",
            "</if>",
            "<if test='startTime == null and endTime != null'>",
            "  AND makeTime &lt;= #{endTime}",
            "</if>",

            // 🔹 产品类型条件
            "<if test='productType != null and productType != \"\"'>",
            "  AND productType = #{productType}",
            "</if>",

            // 🔹 areaCode 不为空时才启用过滤逻辑
            "<if test='areaCode != null and areaCode != \"\"'>",
            "  AND (",
            "      (taskStatus = 0 AND AREACODE = #{areaCode})",
            "   OR (taskStatus IN (1, 2) AND (AREACODE = #{areaCode} OR team LIKE #{teamLike}))",
            "  )",
            "</if>",

            " </script>"
    })
    int getTotalCount(
            @Param("taskName") String taskName,
            @Param("startTime") String startTime,
            @Param("endTime") String endTime,
            @Param("productType") String productType,
            @Param("areaCode") String areaCode,
            @Param("teamLike") String teamLike
    );

    // 🔹 查询总条数
    @Select({
            "<script>",
            "SELECT COUNT(*) FROM T_POINT_FORECAST_RECORD",
            "WHERE 1=1",

            // 🔹 任务名称模糊查询
            "<if test='taskName != null and taskName != \"\"'>",
            "  AND taskName LIKE #{taskName}",
            "</if>",

            // 🔹 时间条件
            "<if test='startTime != null and endTime != null'>",
            "  AND makeTime BETWEEN #{startTime} AND #{endTime}",
            "</if>",
            "<if test='startTime != null and endTime == null'>",
            "  AND makeTime &gt;= #{startTime}",
            "</if>",
            "<if test='startTime == null and endTime != null'>",
            "  AND makeTime &lt;= #{endTime}",
            "</if>",

            // 🔹 产品类型条件
            "<if test='productType != null and productType != \"\"'>",
            "  AND productType = #{productType}",
            "</if>",

            // 🔹 areaCode 不为空时才启用过滤逻辑
            "<if test='areaCode != null and areaCode != \"\"'>",
            "  AND (",

            "  taskStatus =1 AND (AREACODE = #{areaCode} OR team LIKE #{teamLike}) ",
            "  )",
            "</if>",

            " </script>"
    })
    int getStatusOneCount(
            @Param("taskName") String taskName,
            @Param("startTime") String startTime,
            @Param("endTime") String endTime,
            @Param("productType") String productType,
            @Param("areaCode") String areaCode,
            @Param("teamLike") String teamLike
    );


    @Select("SELECT * FROM T_POINT_FORECAST_RECORD where ID=#{id}")
    Records getAllRecordsById(Integer id);


    @Select("SELECT * FROM T_POINT_FORECAST_RECORD WHERE " +
            "MANAGEID=#{manageId}")
    List<Records> getByManageId(Integer manageId);

    @Select("SELECT * FROM T_POINT_FORECAST_RECORD WHERE " +
            "HOURSPAN=#{hourSpan} AND MANAGEID=#{manageId} AND ELEMENT=#{element} AND STATION=#{station} AND MAKETIME=#{makeTime}")
    Records getByManageIdAndElementsAndMakeTimeAndHourSpanAndStations(Records record);
}
