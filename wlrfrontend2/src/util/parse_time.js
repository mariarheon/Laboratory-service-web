export default function parseTime(str) {
  const regex = /^(\d{2}):(\d{2})$/;
  const m = str.match(regex);
  if (!m) {
    return null;
  }
  return {
    hh: +m[1],
    mm: +m[2],
  };
}