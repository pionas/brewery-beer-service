package pl.excellentapp.brewery.beer.application.beer;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.util.Pair;
import pl.excellentapp.brewery.beer.domain.beer.Beer;

import java.util.List;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class BeerPage {

    private List<Beer> beers;
    private int pageNumber;
    private int pageSize;
    private int total;

    public static BeerPage of(Pair<List<Beer>, Integer> list, PageRequest pageRequest) {
        return BeerPage.builder()
                .beers(list.getFirst())
                .total(list.getSecond())
                .pageNumber(pageRequest.getPageNumber())
                .pageSize(pageRequest.getPageSize())
                .build();
    }
}
