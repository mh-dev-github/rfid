package com.mh.amqp.handlers;

/**
 * Implementa el patron Decorator. Este patron permite adicionar funcionalidad dinamicamente a un objeto.
 * 
 * @author arosorio@gmail.com
 *
 * @param <T> Objetoi que debe ser procesado por la cadena de Handlers 
 * 
 * @see <a href="https://sourcemaking.com/design_patterns/decorator">Decorator Design Pattern</a>
 */
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
