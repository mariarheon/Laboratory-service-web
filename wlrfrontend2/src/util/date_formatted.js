import z from './zero_appender';

export default function dateTimeFormatted(dateTime) {
  const d = new Date(dateTime);
  return z(d.getUTCDate(), 2) + '.' + z(d.getUTCMonth() + 1, 2) + '.' + z(d.getUTCFullYear(), 4);
}
