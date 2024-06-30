package xyz.cupscoffee.files.api.driver.teams;

import xyz.cupscoffee.files.api.Disk;
import xyz.cupscoffee.files.api.File;
import xyz.cupscoffee.files.api.Folder;
import xyz.cupscoffee.files.api.SavStructure;
import xyz.cupscoffee.files.api.driver.SavDriver;
import xyz.cupscoffee.files.api.exception.InvalidFormatFileException;
import xyz.cupscoffee.files.api.implementation.SimpleDisk;
import xyz.cupscoffee.files.api.implementation.SimpleFile;
import xyz.cupscoffee.files.api.implementation.SimpleFolder;
import xyz.cupscoffee.files.api.implementation.SimpleSavStructure;
import java.io.*;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class DiegOsDriver implements SavDriver {

    @Override
    public SavStructure readSavFile(InputStream fileInputStream) throws InvalidFormatFileException {

        byte[] CONTENT_BUFFER;

        try {
            CONTENT_BUFFER = fileInputStream.readAllBytes();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        Disk[] disks = buildDisks(CONTENT_BUFFER);

        HashMap<String, String> metadata = new HashMap<>();

        return new SimpleSavStructure("DiegOsDriver", disks, metadata);
    }

    private Disk[] buildDisks(byte[] CONTENT_BUFFER) {

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(CONTENT_BUFFER);
        Object obj;

        try {
            ObjectInputStream ois = new ObjectInputStream(byteArrayInputStream);
            obj = ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        DiegOsSavContent content = (DiegOsSavContent) obj;

        Disk[] disks = new Disk[content.disks.length];

        for (int i = 0; i < disks.length; i++) disks[i] = convertToCoffeeDisk(content.disks[i]);

        return disks;
    }

    private Disk convertToCoffeeDisk(DiegOsDisk disk) {
        return new SimpleDisk(disk.name, convertToCoffeeFolder(disk.getRoot()), disk.limitSize, disk.meta);
    }

    private Folder convertToCoffeeFolder(DiegOsFolder folder) {
        return new SimpleFolder(
                folder.name,
                folder.files.stream().map(this::convertToCoffeeFile).collect(Collectors.toList()),
                folder.folders.stream().map(this::convertToCoffeeFolder).collect(Collectors.toList()),
                folder.created,
                folder.lastModified,
                new Path() {
                    @Override
                    public FileSystem getFileSystem() {
                        return null;
                    }

                    @Override
                    public boolean isAbsolute() {
                        return false;
                    }

                    @Override
                    public Path getRoot() {
                        return null;
                    }

                    @Override
                    public Path getFileName() {
                        return null;
                    }

                    @Override
                    public Path getParent() {
                        return null;
                    }

                    @Override
                    public int getNameCount() {
                        return 0;
                    }

                    @Override
                    public Path getName(int index) {
                        return null;
                    }

                    @Override
                    public Path subpath(int beginIndex, int endIndex) {
                        return null;
                    }

                    @Override
                    public boolean startsWith(Path other) {
                        return false;
                    }

                    @Override
                    public boolean endsWith(Path other) {
                        return false;
                    }

                    @Override
                    public Path normalize() {
                        return null;
                    }

                    @Override
                    public Path resolve(Path other) {
                        return null;
                    }

                    @Override
                    public Path relativize(Path other) {
                        return null;
                    }

                    @Override
                    public URI toUri() {
                        return null;
                    }

                    @Override
                    public Path toAbsolutePath() {
                        return null;
                    }

                    @Override
                    public Path toRealPath(LinkOption... options) throws IOException {
                        return null;
                    }

                    @Override
                    public WatchKey register(WatchService watcher, WatchEvent.Kind<?>[] events, WatchEvent.Modifier... modifiers) throws IOException {
                        return null;
                    }

                    @Override
                    public int compareTo(Path other) {
                        return 0;
                    }
                },
                folder.metaData);
    }

    private File convertToCoffeeFile(DiegOsFile file) {
        return new SimpleFile(file.name, file.content, file.created, file.lastModified, new Path() {
            @Override
            public FileSystem getFileSystem() {
                return null;
            }

            @Override
            public boolean isAbsolute() {
                return false;
            }

            @Override
            public Path getRoot() {
                return null;
            }

            @Override
            public Path getFileName() {
                return null;
            }

            @Override
            public Path getParent() {
                return null;
            }

            @Override
            public int getNameCount() {
                return 0;
            }

            @Override
            public Path getName(int index) {
                return null;
            }

            @Override
            public Path subpath(int beginIndex, int endIndex) {
                return null;
            }

            @Override
            public boolean startsWith(Path other) {
                return false;
            }

            @Override
            public boolean endsWith(Path other) {
                return false;
            }

            @Override
            public Path normalize() {
                return null;
            }

            @Override
            public Path resolve(Path other) {
                return null;
            }

            @Override
            public Path relativize(Path other) {
                return null;
            }

            @Override
            public URI toUri() {
                return null;
            }

            @Override
            public Path toAbsolutePath() {
                return null;
            }

            @Override
            public Path toRealPath(LinkOption... options) throws IOException {
                return null;
            }

            @Override
            public WatchKey register(WatchService watcher, WatchEvent.Kind<?>[] events, WatchEvent.Modifier... modifiers) throws IOException {
                return null;
            }

            @Override
            public int compareTo(Path other) {
                return 0;
            }
        }, file.meta);
    }

    private record DiegOsSavContent(DiegOsDisk[] disks) implements Serializable {

    }

    private static class DiegOsDisk implements Serializable {

        private String name;
        private DiegOsFolder root;
        private long limitSize;
        private long occupied;
        private Map<String, String> meta;

        public DiegOsDisk(String name, DiegOsFolder root, long limitSize, long occupied, Map<String, String> meta) {
            this.name = name;
            this.root = root;
            this.limitSize = limitSize;
            this.occupied = occupied;
            this.meta = meta;
        }

        public String getName() {
            return name;
        }

        public DiegOsFolder getRoot() {
            return root;
        }

        public long getLimitSize() {
            return limitSize;
        }

        public long getOccupied() {
            return occupied;
        }

        public Map<String, String> getMeta() {
            return meta;
        }
    }

    private static class DiegOsFolder implements Serializable {

        private String name;
        private List<DiegOsFile> files = new ArrayList<>();
        private List<DiegOsFolder> folders = new ArrayList<>();
        private LocalDateTime created;
        private LocalDateTime lastModified;
        private long size;
        private Map<String, String> metaData;

        public DiegOsFolder(String name,
                            List<DiegOsFile> files,
                            List<DiegOsFolder> folders,
                            LocalDateTime created,
                            LocalDateTime lastModified,
                            Map<String, String> metaData) {
            Objects.requireNonNull(name, "Name cannot be null");
            Objects.requireNonNull(files, "Files cannot be null");
            Objects.requireNonNull(folders, "Folders cannot be null");
            Objects.requireNonNull(created, "Created cannot be null");
            Objects.requireNonNull(lastModified, "Last modified cannot be null");
            Objects.requireNonNull(metaData, "Metadata cannot be null");

            this.name = name;
            this.files = files;
            this.folders = folders;
            this.created = created;
            this.lastModified = lastModified;
            this.size = files.stream().mapToLong(DiegOsFile::getSize).sum() + folders.stream().mapToLong(DiegOsFolder::getSize).sum();
            this.metaData = metaData;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<DiegOsFile> getFiles() {
            return files;
        }

        public LocalDateTime getCreatedDateTime() {
            return this.created;
        }

        public LocalDateTime getLastModifiedDateTime() {
            return this.lastModified;
        }

        public void setLastModifiedDateTime(LocalDateTime lastModified) {
            this.lastModified = lastModified;
        }

        public long getSize() {
            return this.size;
        }

        public Map<String, String> getOtherMeta() {
            return this.metaData;
        }

        public List<DiegOsFolder> getFolders() {
            return this.folders;
        }

    }

    private static class DiegOsFile implements Serializable {

        private String name;
        private ByteBuffer content;
        private LocalDateTime created;
        private LocalDateTime lastModified;
        private long size;
        private Map<String, String> meta;

        public DiegOsFile(String name,
                          ByteBuffer content,
                          LocalDateTime created,
                          LocalDateTime lastModified,
                          Map<String, String> meta) {
            Objects.requireNonNull(name, "Name cannot be null");
            Objects.requireNonNull(content, "Content cannot be null");
            Objects.requireNonNull(created, "Created cannot be null");
            Objects.requireNonNull(lastModified, "Last modified cannot be null");
            Objects.requireNonNull(meta, "Metadata cannot be null");

            this.name = name;
            this.content = content;
            this.created = created;
            this.lastModified = lastModified;
            this.size = content.capacity();
            this.meta = meta;
        }

        public String getName() {
            return this.name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public ByteBuffer getContent() {
            return this.content;
        }

        public void setContent(ByteBuffer content) {
            this.size = content.capacity();
            this.content = content;
        }

        public LocalDateTime getCreatedDateTime() {
            return this.created;
        }

        public LocalDateTime getLastModifiedDateTime() {
            return this.lastModified;
        }

        public void setLastModifiedDateTime(LocalDateTime lastModified) {
            this.lastModified = lastModified;
        }

        public long getSize() {
            return this.size;
        }

        public Map<String, String> getOtherMeta() {
            return this.meta;
        }

    }

}
