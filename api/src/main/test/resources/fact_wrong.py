def fact(n):
    if n == 1:
        return 0
    else:
        return n * fact(n - 1)