package com.pxxy.utils;


import com.github.pagehelper.PageInfo;
import com.pxxy.constant.ResponseMessage;
import com.pxxy.entity.pojo.County;
import lombok.Data;

import java.io.Serializable;
import java.util.AbstractList;
import java.util.List;

@Data
public class ResultResponse<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer code;
    private String msg;
    private T data;

    public ResultResponse() {
    }

    private ResultResponse(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static ResultResponse<?> ok() {
        return new ResultResponse<>(ResponseMessage.OK_CODE, ResponseMessage.OK_MSG, null);
    }

    public static <T> ResultResponse<T> ok(T data) {
        return new ResultResponse<>(ResponseMessage.OK_CODE, ResponseMessage.OK_MSG, data);
    }

    public static <T> ResultResponse<T> ok(String msg) {
        return new ResultResponse<>(ResponseMessage.OK_CODE, msg, null);
    }

    public static <T> ResultResponse<T> ok(String msg, T data) {
        return new ResultResponse<>(ResponseMessage.OK_CODE, msg, data);
    }

    public static <T> ResultResponse<T> fail(String msg) {
        return new ResultResponse<>(ResponseMessage.FAIL_CODE, msg, null);
    }

    public static <T> ResultResponse<T> fail(int errorCode, String msg) {
        return new ResultResponse<>(errorCode, msg, null);
    }

    public static <T> ResultResponse<T> fail(int errorCode, String msg, T data) {
        return new ResultResponse<>(errorCode, msg, data);
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public String toString() {
        String fmt = "ResultResponse(code=" + code + ", msg=" + msg + ", data=";
        if (data instanceof List) {
            Object[] array = ((List<?>) data).toArray();
            return fmt + ToStringUtil.toString(array) + ')';
        }
        if (data instanceof PageInfo) {
            return fmt +
                    "PageInfo{pageNum=" + ((PageInfo<?>) data).getPageNum() +
                    ", pageSize=" + ((PageInfo<?>) data).getPageSize() +
                    ", total=" + ((PageInfo<?>) data).getTotal() +
                    ", list=" + new MockList(((PageInfo<?>) data).getList()) + "})";
        }
        return fmt + ToStringUtil.toString(data) + ')';
    }

    @SuppressWarnings("unchecked")
    private static class MockList<T> extends AbstractList<T> {

        private final Object[] elements;

        public MockList(List<T> list) {
            elements = list.toArray();
        }

        @Override
        public T get(int index) {
            return (T) elements[index];
        }

        @Override
        public int size() {
            return elements.length;
        }

        @Override
        public String toString() {
            return ToStringUtil.toString(elements);
        }
    }

    public static void main(String[] args) {
        System.out.println(ResultResponse.ok(new County().setCouName("afughaieruhfgakjgnuiarfhawjehfrafughaieruhfgakjgnuiarfhawjehfrafughaieruhfgakjgnuiarfhawjehfrafughaieruhfgakjgnuiarfhawjehfrafughaieruhfgakjgnuiarfhawjehfrafughaieruhfgakjgnuiarfhawjehfrafughaieruhfgakjgnuiarfhawjehfrafughaieruhfgakjgnuiarfhawjehfrafughaieruhfgakjgnuiarfhawjehfrafughaieruhfgakjgnuiarfhawjehfrafughaieruhfgakjgnuiarfhawjehfrafughaieruhfgakjgnuiarfhawjehfrafughaieruhfgakjgnuiarfhawjehfr")));
    }

}

