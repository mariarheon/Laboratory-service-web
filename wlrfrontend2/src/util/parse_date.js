export default function parseDate(str) {
  const regex = /^(\d{2})\.(\d{2})\.(\d{4})$/;
  if (!str.match(regex)) {
    return null;
  }
  return str.replace(regex, (whole, d, m, y) => `${y}-${m}-${d}T00:00:00.000+00:00`);
}