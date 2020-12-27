package com.spbstu.weblabresearch.services;

import com.spbstu.weblabresearch.dbo.Request;
import com.spbstu.weblabresearch.dbo.RequestStatus;
import com.spbstu.weblabresearch.dbo.User;
import com.spbstu.weblabresearch.dto.common.RequestDto;
import com.spbstu.weblabresearch.dto.response.SuccessDto;
import com.spbstu.weblabresearch.repos.AnalysisRepository;
import com.spbstu.weblabresearch.repos.RequestRepository;
import com.spbstu.weblabresearch.util.StringTypeVerifier;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RequestService {
    @NonNull
    RequestRepository requests;

    public List<RequestDto> findAll() {
        return requests.findAll().stream().map(RequestDto::new).collect(Collectors.toList());
    }

    public List<RequestDto> findByClient(final User client) {
        return requests
                .findByClient(client)
                .stream()
                .map(RequestDto::new)
                .collect(Collectors.toList());
    }

    public Optional<RequestDto> findById(final int id) {
        return requests.findById(id).map(RequestDto::new);
    }

    public SuccessDto edit(final RequestDto requestDto) {
        try {
            validate(requestDto);
        } catch (Exception ex) {
            return new SuccessDto(ex.getMessage());
        }
        Optional<Request> requestOpt = requests.findById(requestDto.getId());
        if (requestOpt.isEmpty()) {
            return new SuccessDto("Не найден");
        }
        Request request = requestOpt.get();
        request.setSurname(requestDto.getSurname());
        request.setName(requestDto.getName());
        request.setPatronymic(requestDto.getPatronymic());
        request.setSex(requestDto.getSex());
        request.setPassportSeries(requestDto.getPassportSeries());
        request.setPassportNumber(requestDto.getPassportNumber());
        request.setArrivalTime(requestDto.getArrivalTime());
        request.setAnalysisList(requestDto.getAnalysisList());
        request.setStatus(RequestStatus.CREATED);
        requests.save(request);
        return new SuccessDto();
    }

    public SuccessDto add(final User client,
                          final RequestDto requestDto) {
        try {
            validate(requestDto);
        } catch (Exception ex) {
            return new SuccessDto(ex.getMessage());
        }
        Request request = new Request();
        request.setClient(client);
        request.setSurname(requestDto.getSurname());
        request.setName(requestDto.getName());
        request.setPatronymic(requestDto.getPatronymic());
        request.setSex(requestDto.getSex());
        request.setPassportSeries(requestDto.getPassportSeries());
        request.setPassportNumber(requestDto.getPassportNumber());
        request.setArrivalTime(requestDto.getArrivalTime());
        request.setAnalysisList(requestDto.getAnalysisList());
        request.setStatus(RequestStatus.CREATED);
        requests.save(request);
        return new SuccessDto();
    }

    private void validate(RequestDto request) throws Exception {
        StringTypeVerifier.notEmptyString(request.getSurname(), "Фамилия должна быть указана");
        StringTypeVerifier.notEmptyString(request.getName(), "Имя должно быть указано");
        StringTypeVerifier.notEmptyString(request.getPatronymic(), "Отчество должно быть указано");
        if (request.getAnalysisList().size() <= 0) {
            throw new Exception("Необходимо выбрать хотя бы 1 анализ");
        }
    }
}
