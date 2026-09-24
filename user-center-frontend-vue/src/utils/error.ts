export interface ApiErrorResponse {
  message?: string;
  response?: {
    data?: {
      description?: string;
      message?: string;
    };
  };
}

export const extractErrorMessage = (
  error: unknown,
  fallback = "操作失败，请稍后重试"
): string => {
  if (typeof error === "string") {
    return error;
  }
  if (typeof error === "object" && error !== null) {
    const apiError = error as ApiErrorResponse;
    return (
      apiError.response?.data?.description ||
      apiError.response?.data?.message ||
      apiError.message ||
      fallback
    );
  }
  return fallback;
};
