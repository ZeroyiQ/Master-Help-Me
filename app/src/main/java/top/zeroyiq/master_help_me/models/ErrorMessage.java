package top.zeroyiq.master_help_me.models;


import java.io.Serializable;
import java.util.Arrays;

/**
 * 返回错误信息模型
 * Created by ZeroyiQ on 2017/6/4.
 */

public class ErrorMessage extends BaseRecord {

    private Error error = new Error();

    public ErrorMessage() {
    }

    public ErrorMessage(String message) {
        this.error = new Error(message);
    }

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }

    public ErrorException toException(){
        return  new ErrorException(this);
    }

    /**
     * 自定义错误
     */
    private static class Error extends BaseRecord {
        private String message;
        private Long code = 0L;
        private String[] trace; //痕迹
        private Context context;

        public Error(String message) {
            this.message = message;
        }

        public Error() {
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public Long getCode() {
            return code;
        }

        public void setCode(Long code) {
            this.code = code;
        }

        public String[] getTrace() {
            return trace;
        }

        public void setTrace(String[] trace) {
            this.trace = trace;
        }

        public Context getContext() {
            return context;
        }

        public void setContext(Context context) {
            this.context = context;
        }

        public static class Context implements Serializable {
            private String[] email = new String[0];
            private String[] password = new String[0];

            public Context() {
            }

            @Override
            public String toString() {
                return "Context{" +
                        "email=" + Arrays.toString(email) +
                        ", password=" + Arrays.toString(password) +
                        '}';
            }

            public String[] getEmail() {
                return email;
            }

            public void setEmail(String[] email) {
                this.email = email;
            }

            public String[] getPassword() {
                return password;
            }

            public void setPassword(String[] password) {
                this.password = password;
            }
        }


    }

    private static class ErrorException extends Exception {
        private ErrorMessage errorMessage;

        public ErrorException(ErrorMessage errorMessage) {
            super(errorMessage.getError().getMessage());
            if (errorMessage == null) {
                this.errorMessage = new ErrorMessage("未知错误");
            } else {
                this.errorMessage = errorMessage;
            }
        }
    }

}

