package com.att.tdp.popcorn_palace.service;

import com.att.tdp.popcorn_palace.dto.request.ShowtimeRequest;
import com.att.tdp.popcorn_palace.dto.response.ShowtimeResponse;
import java.util.List;

public interface ShowtimeService {
    ShowtimeResponse getShowtimeById(Long id);
    ShowtimeResponse addShowtime(ShowtimeRequest request);
    void updateShowtime(Long id, ShowtimeRequest request);
    void deleteShowtime(Long id);
} 