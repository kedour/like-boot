package com.like.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * Created with IntelliJ IDEA.
 *
 * @ClassName BaseEntity
 * @Description
 * @Author like
 * @Date 2020/10/12 17:35
 * @Version v1.0
 */


@SuppressWarnings("serial")
@Setter
@Getter
public class BaseEntity<T>  extends Model implements Serializable {
    @Id
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @TableId(type= IdType.ID_WORKER)
    @ApiModelProperty(value = "id")
    public  Long id;
    @ApiModelProperty(value = "创建时间")
    public  String createTime;
    @ApiModelProperty(value = "修改时间")
    public  String updateTime;
    @ApiModelProperty(value = "创建人")
    public  String creator;
    @ApiModelProperty(value = "修改人")
    public  String updater;
    //排序
    @ApiModelProperty(value = "排序")
    public  Integer sort;
    //是否已删除
    @ApiModelProperty(value = "是否已删除")
    public  Integer delFlag;
    //设置创建时间 与修改时间默认值
    public void initTime(){
        delFlag = 0;
        try {
            Date nowdate = new Date();
            // 格式
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if(this.getCreateTime()==null){
                setCreateTime(format
                        .format(nowdate));
            }
            setUpdateTime(format
                    .format(nowdate));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updTime(){
        delFlag = 0;
        try {
            Date nowdate = new Date();
            // 格式
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            setUpdateTime(format
                    .format(nowdate));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

