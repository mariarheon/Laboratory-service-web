export default function requestStatusFormatted(status) {
  if (status === 'CREATED') {
    return 'На рассмотрении';
  }
  if (status === 'APPLIED') {
    return 'Принята';
  }
  if (status === 'IN_WORK') {
    return 'В работе';
  }
  if (status === 'CANCELED') {
    return 'Отменена';
  }
  if (status === 'FINISHED') {
    return 'Завершена';
  }
  if (status === 'CLIENT_IS_AWARE') {
    return 'Клиент осведомлён';
  }
  return '';
}
