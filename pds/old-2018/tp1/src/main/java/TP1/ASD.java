package TP1;

import java.util.List;

public class ASD {
    static public class Document {
        private List<Triplet> triplets;

        public Document(List<Triplet> triplets) {
            this.triplets = triplets;
        }

        public String toNtriples() {
            return triplets.stream()
                    .map(Triplet::toNtriples)
                    .reduce((str1, str2) -> str1 + str2)
                    .orElse("Fail");
        }
    }

    public static class Triplet {
        private Entity entity;
        private List<Body> bodies;

        public Triplet(Entity entity, List<Body> bodies) {
            this.entity = entity;
            this.bodies = bodies;
        }

        public String toNtriples() {
            StringBuilder sb = new StringBuilder();
            for (Body body : bodies) {
                for (ABCObject abcObject : body.objects)
                    sb.append(this.entity.toNtriples())
                            .append(" ")
                            .append(body.entity.toNtriples())
                            .append(" ")
                            .append(abcObject.toNtriples())
                            .append(" .\n");
            }
            return sb.toString();
        }
    }

    public static class Body {
        private Entity entity;
        private List<ABCObject> objects;

        public Body(Entity entity, List<ABCObject> objects) {
            this.entity = entity;
            this.objects = objects;
        }
    }

    public static abstract class ABCObject {
        public abstract String toNtriples();

        public static class ObjectEntity extends ABCObject {
            private Entity entity;

            public ObjectEntity(Entity entity) {
                this.entity = entity;
            }

            public String toNtriples() {
                return this.entity.toNtriples();
            }
        }

        public static class ObjectString extends ABCObject {
            private String string;

            public ObjectString(String string) {
                this.string = string;
            }

            public String toNtriples() {
                return '"' + this.string + '"';
            }
        }
    }

    public static class Entity {
        private String string;

        public Entity(String string) {
            this.string = string;
        }

        public String toNtriples() {
            return '<' + this.string + '>';
        }
    }
}
