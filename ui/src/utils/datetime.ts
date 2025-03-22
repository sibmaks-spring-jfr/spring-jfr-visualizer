export const toISOString = (millis: number | null | undefined): string => {
  if (!millis) {
    return 'Unknown';
  }
  const rawDate = new Date(millis);
  return rawDate.toISOString();
};
