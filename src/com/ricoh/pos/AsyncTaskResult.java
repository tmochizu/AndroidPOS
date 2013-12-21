package com.ricoh.pos;

public class AsyncTaskResult<T> {
	private T content;
	private int resId;
	private boolean isError;

	private AsyncTaskResult(T content, boolean isError, int resId) {
		this.content = content;
		this.isError = isError;
		this.resId = resId;
	}

	public T getContent() {
		return content;
	}

	public boolean isError() {
		return isError;
	}

	public int getResourceId() {
		return resId;
	}

	public static <T> AsyncTaskResult<T> createNormalResult(T content) {
		return new AsyncTaskResult<T>(content, false, 0);
	}

	public static <T> AsyncTaskResult<T> createErrorResult(int resId) {
		return new AsyncTaskResult<T>(null, true, resId);
	}
}
