import dateFormatted from './date_formatted';
import z from './zero_appender';

export default function dateTimeFormatted(dateTime) {
  const d = new Date(dateTime);
  return dateFormatted(dateTime) + ' ' + z(d.getUTCHours(), 2) + ':' + z(d.getUTCMinutes(), 2);
}
