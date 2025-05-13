package org.example.common.result;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.common.enums.CommonEnum;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Result<T> {

    private Integer code;//业务状态码
    private String message;//提示信息
    private T data;//响应数据

    /*
    * 成功（普通）
    * */
    public static <E> Result<E> success(E data)
    {
        return new Result<>(CommonEnum.SUCCESS.getCode(), CommonEnum.SUCCESS.getMessage(),data);
    }

    /*
    * 成功（含参）
    * */
    public static <E> Result<E> success(int code ,String message,E data)
    {
        return new Result<>(code,message,data);
    }
    /*
    * 成功（不含参）
    * */
    public static Result success()
    {
        return new Result<>(CommonEnum.SUCCESS.getCode(), CommonEnum.SUCCESS.getMessage(),null);
    }

    /*
    * 失败（普通）
    * */
    public static <E> Result<E> fail(E data)
    {
        return new Result<>(CommonEnum.FAIL.getCode(), CommonEnum.FAIL.getMessage(),data);
    }
    public static <E> Result<E> fail(int code ,String message,E data)
    {
        return new Result<>(code,message,data);
    }

    public static Result fail()
    {
        return new Result<>(CommonEnum.FAIL.getCode(), CommonEnum.FAIL.getMessage(),null);
    }

    /*
    * 操作异常
    * */
    public static <E> Result<E> operateException(E data)
    {
        return new Result<>(CommonEnum.FAIL.getCode(), CommonEnum.FAIL.getMessage(),data);
    }
    public static <E> Result<E> operateException(int code ,String message,E data)
    {
        return new Result<>(code,message,data);
    }
    public static <E> Result<E> operateException()
    {
        return new Result<>(CommonEnum.SUCCESS.getCode(), CommonEnum.SUCCESS.getMessage(),null);
    }

    public static Result fail(int i, String s) {
        return new Result(i,s,null);
    }
    public static Result operationException(int i, String s) {
        return new Result(i,s,null);
    }
}
