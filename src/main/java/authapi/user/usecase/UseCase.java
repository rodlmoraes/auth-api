package authapi.user.usecase;

public interface UseCase<I extends UseCase.Input, O extends UseCase.Output> {
    O execute(I input);

    interface Input {}
    interface Output {}
}
