package dev.kriti.ragone.Response;

public class CommonApiResponse {
	private Object responseMessage;
	private boolean isSuccess;

	public Object getResponseMessage() {
		return responseMessage;
	}

	public void setResponseMessage(Object responseMessage) {
		this.responseMessage = responseMessage;
	}

	public boolean isSuccess() {
		return isSuccess;
	}

	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

}
