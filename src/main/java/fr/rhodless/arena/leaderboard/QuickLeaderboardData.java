package fr.rhodless.arena.leaderboard;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class QuickLeaderboardData<T> {
    private final String playerName;
    private final T value;
}
