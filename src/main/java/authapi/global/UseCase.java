package authapi.global;

public interface UseCase<I extends UseCase.Input, O extends UseCase.Output> {
    O execute(I input);

    interface Input {}
    interface Output {}
}
