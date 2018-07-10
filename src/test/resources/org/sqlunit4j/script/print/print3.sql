def foo=123;
def bar=123;
--@Verify
print foo bar;
print 'foo:${foo} ${foo} bar:${bar}';