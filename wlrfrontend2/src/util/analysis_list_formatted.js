export default function analysisListFormatted(analysisList) {
  return '[' + analysisList.map(x => x.name).join(',') + ']';
}