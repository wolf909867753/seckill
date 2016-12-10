package com.wolf.exception;

/**
 * 秒杀关闭异常，当秒杀结束时用户还要进行秒杀就会出现这个异常
 * Created by codingBoy on 16/11/27.
 */
public class SeckillCloseException extends SeckillException{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SeckillCloseException(String message) {
        super(message);
    }

    public SeckillCloseException(String message, Throwable cause) {
        super(message, cause);
    }
}
