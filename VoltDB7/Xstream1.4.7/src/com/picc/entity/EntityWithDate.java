package com.picc.entity;

import java.util.Date;

/**
 * Created by hunter on 2018/2/2.
 */
public class EntityWithDate {
//util.date
private Date date;
private Date dateForSqlDate;
//用来接受timestamp的值
private Date dateForTimestamp;

public Date getDate() {
        return date;
        }

public void setDate(Date date) {
        this.date = date;
        }

public Date getDateForSqlDate() {
        return dateForSqlDate;
        }

public void setDateForSqlDate(Date dateForSqlDate) {
        this.dateForSqlDate = dateForSqlDate;
        }

public Date getDateForTimestamp() {
        return dateForTimestamp;
        }

public void setDateForTimestamp(Date dateForTimestamp) {
        this.dateForTimestamp = dateForTimestamp;
        }
        }
