package com.mh.core.patterns;

public abstract class AbstractHandler<T> {
	protected AbstractHandler<T> nextHandler;

	public void setNextHandler(AbstractHandler<T> nextHandler) {
		if (this.nextHandler == null) {
			this.nextHandler = nextHandler;
		} else {
			if (!this.nextHandler.equals(nextHandler)) {
				this.nextHandler.setNextHandler(nextHandler);
			}
		}
	}

	public void receiveRequest(T request) {
		if (canHandleRequest(request)) {
			handleRequest(request);
		}
		if (nextHandler != null) {
			nextHandler.receiveRequest(request);
		}
	}

	abstract protected boolean canHandleRequest(T request);

	abstract protected void handleRequest(T request);
}
