package ru.otus.dao;

import ru.otus.converter.Converter;
import ru.otus.converter.ConverterCSV;
import ru.otus.dataservice.ReaderResource;
import ru.otus.model.Question;

import java.util.List;
import java.util.stream.Collectors;


public class QuestDaoCsv implements QuestDao {
    private final ReaderResource dataReaderResource;

    private final Converter converter;

    public QuestDaoCsv(ReaderResource dataReaderResource, Converter converter) {
        this.dataReaderResource = dataReaderResource;
        this.converter = converter;
    }

    @Override
    public List<Question> getAll() {
        return dataReaderResource.getLines().stream()
                .map(converter::covertToQuest)
                .collect(Collectors.toList());
    }

}
