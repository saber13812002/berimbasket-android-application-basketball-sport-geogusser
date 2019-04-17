package ir.berimbasket.app.data.network.model;

import com.google.gson.annotations.SerializedName;

public class ValidateResponse {

    @SerializedName("code")
    private String code;
    @SerializedName("data")
    private Data data;

    public ValidateResponse(String code, Data data) {
        this.code = code;
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data {
        @SerializedName("status")
        private int status;

        private Data(int status) {
            this.status = status;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }
}
