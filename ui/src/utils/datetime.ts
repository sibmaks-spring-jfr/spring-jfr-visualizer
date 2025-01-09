export const toLocalDateTime = (millis: number | null): string => {
  if (!millis) {
    return 'Unknown';
  }
  const rawDate = new Date(millis);
  return rawDate.toLocaleDateString() + ' ' + rawDate.toLocaleTimeString();
};

export const toISOString = (millis: number | null): string => {
  if (!millis) {
    return 'Unknown';
  }
  const rawDate = new Date(millis);
  return rawDate.toISOString();
};
