package ru.skirda.Presentation.Console;

import java.util.Optional;

/**
* Интерфейс для провайдера сценариев
*/
public interface IScenarioProvider {
    /**
     * Пытается получить сценарий
     *
     * @return Возвращает Optional сценария, если он доступен, иначе пустой Optional
     */
    Optional<IScenario> tryGetScenario();
}
