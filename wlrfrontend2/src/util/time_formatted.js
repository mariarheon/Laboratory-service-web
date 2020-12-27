import z from './zero_appender';

export default function timeFormatted(myTime) {
  return z(myTime.hour, 2) + ':' + z(myTime.minute, 2);
}
