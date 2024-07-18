export const getMediaSource = (url) => {
  if (url?.startsWith("http")) {
    return url;
  }
  return `data:video/mp4;base64,${url}`;
};
