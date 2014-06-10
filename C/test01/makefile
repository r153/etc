# makefile C

CC = gcc
CFLAGS =
LDFLAGS =
INCLUDES = -I/usr/local/include
LIBS = 
OBJS = src.o

TARGET = app
all: $(TARGET)

$(TARGET): $(OBJS)
	$(CC) $(LDFLAGS) -o $@ $(OBJS) $(LIBS)
 
.c.o:
	$(CC) $(CFLAGS) $(INCLUDES) -c $<

clean:
	-rm -f $(TARGET) $(OBJS)
