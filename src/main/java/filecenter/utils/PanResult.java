package filecenter.utils;

public class PanResult<T> {
    int resultCode=1;
    String resultMsg="成功";
    T resultData;

    public int getResultCode() {
        return resultCode;
    }

    public String getResultMsg() {
        return resultMsg;
    }

    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }

    public T getResultData() {
        return resultData;
    }

    public void setResultData(T resultData) {
        this.resultData = resultData;
    }

    public void success(T object){
        resultData=object;
    }
    public void error(String msg){
        resultCode=0;
        resultMsg=msg;
    }

}
