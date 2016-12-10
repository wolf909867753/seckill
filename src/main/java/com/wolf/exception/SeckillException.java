package com.wolf.exception;

/**
 * 秒杀相关的所有业务异常
 * Created by codingBoy on 16/11/27.
 */
public class SeckillException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SeckillException(String message) {
        super(message);
    }

    public SeckillException(String message, Throwable cause) {
        super(message, cause);
    }
}
