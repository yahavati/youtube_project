import api from "./api";

export const register = async (userData) => {
  const response = await api.post("/auth/register", userData, {
    headers: {
      "Content-Type": "multipart/form-data",
    },
  });
  return response.data;
};

export const login = async (userData) => {
  const response = await api.post("/auth/login", userData);
  return response.data;
};

export const logout = async () => {
  const response = await api.post("/auth/logout");
  return response.data;
};
