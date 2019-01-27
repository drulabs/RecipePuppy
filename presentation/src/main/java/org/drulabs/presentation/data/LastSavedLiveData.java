package org.drulabs.presentation.data;

import org.drulabs.domain.entities.DomainRecipe;
import org.drulabs.domain.usecases.base.SingleUseCase;
import org.drulabs.presentation.custom.SingleLiveEvent;
import org.drulabs.presentation.entities.PresentationRecipe;
import org.drulabs.presentation.mapper.PresentationMapper;

import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableSingleObserver;

public class LastSavedLiveData extends SingleLiveEvent<Model<PresentationRecipe>> {

    private SingleUseCase<DomainRecipe, Void> singleUseCase;
    private PresentationMapper<DomainRecipe> mapper;
    private SingleObserver singleObserver;

    public LastSavedLiveData(@NonNull PresentationMapper<DomainRecipe> mapper,
                             @NonNull SingleUseCase<DomainRecipe, Void> singleUseCase) {
        this.mapper = mapper;
        this.singleUseCase = singleUseCase;
        this.singleObserver = new SingleObserver();
    }

    @Override
    protected void onActive() {
        postValue(Model.loading(true));
        if (singleObserver.isDisposed()) {
            singleObserver = new SingleObserver();
        }
        singleUseCase.run(singleObserver, null);
    }

    @Override
    protected void onInactive() {
        singleObserver.dispose();
    }

    private class SingleObserver extends DisposableSingleObserver<DomainRecipe> {
        @Override
        public void onSuccess(DomainRecipe domainRecipe) {
            PresentationRecipe presentationRecipe = mapper.mapFrom(domainRecipe);
            postValue(Model.success(presentationRecipe));
        }

        @Override
        public void onError(Throwable e) {
            postValue(Model.error(e));
        }
    }
}
