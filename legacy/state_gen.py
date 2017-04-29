import sys

def gen():
	kinds = ['CLASS', 'EXTENDS', 'IMPLEMENTS', 'STATIC', 'IMPORT', 'PACKAGE', 'INTERFACE', 'THIS', 'LENGTH',
        'TRUE', 'FALSE', 'PUBLIC', 'PROTECTED', 'PRIVATE', 'ABSTRACT', 'FINAL', 'NATIVE', 'SUPER', 'RETURN',
        'IF', 'ELSE', 'WHILE', 'FOR', 'BOOLEAN', 'INT', 'CHAR', 'BYTE', 'SHORT', 'ARRAY', 'NULL', 'ESCAPE',
        'INSTANCEOF', 'MAIN', 'NEW', 'VOID', 'BREAK']
	kindset = set([])
	for kind in kinds:
		for x in range(len(kind)):
			if kind[:x+1] in kindset:
				continue
			kindset.add(kind[:x+1])
			sys.stdout.write(kind[:x+1])
			if x+1 == len(kind):
				sys.stdout.write('(common.TokenType.')
				sys.stdout.write(kind)
				sys.stdout.write('), ')
			else:
				sys.stdout.write('(null), ')
			
		print

if __name__ == '__main__':
	gen()
