
package com.example.laptophome.bank_eldam.data.model.retrivepassword;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Retrivepassword {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("data")
    @Expose
    private RetrivePasswordData data;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public RetrivePasswordData getData() {
        return data;
    }

    public void setData(RetrivePasswordData data) {
        this.data = data;
    }

}
