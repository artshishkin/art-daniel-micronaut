package net.shyshkin.study.micronaut;

import builders.dsl.spreadsheet.query.api.SpreadsheetCriteria;
import builders.dsl.spreadsheet.query.api.SpreadsheetCriteriaResult;
import builders.dsl.spreadsheet.query.poi.PoiSpreadsheetCriteria;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import net.shyshkin.study.micronaut.service.BookExcelService;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@MicronautTest
public class DownloadExcelTest {

    @Inject
    @Client("/")
    HttpClient client;


    @Test
    void booksCanBeDownloadedAsAnExcelFile() throws FileNotFoundException {
        //given
        HttpRequest<?> request = HttpRequest
                .GET("/excel")
                .accept("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        //when
        HttpResponse<byte[]> response = client.toBlocking().exchange(request, byte[].class);

        //then
        assertEquals(HttpStatus.OK, response.status());
        byte[] content = response.body();
        assertNotNull(content);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(content);
        SpreadsheetCriteria query = PoiSpreadsheetCriteria.FACTORY.forStream(inputStream);
        SpreadsheetCriteriaResult result = query.query(workbookCriterion -> {
            workbookCriterion.sheet(BookExcelService.SHEET_NAME, sheetCriterion ->
                    sheetCriterion.row(rowCriterion ->
                            rowCriterion.cell(cellCriterion -> cellCriterion.value("Building Microservices"))));
        });
        assertEquals(result.getCells().size(), 1);
    }
}
