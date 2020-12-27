export default function zeroAppender(s, cnt) {
  s += '';
  let zeros = '';
  const zeroCnt = cnt - s.length;
  for (let i = 0; i < zeroCnt; i++) {
    zeros += '0';
  }
  return zeros + s;
}