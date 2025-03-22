// Code generated by protoc-gen-ts_proto. DO NOT EDIT.
// versions:
//   protoc-gen-ts_proto  v2.6.1
//   protoc               v5.29.3
// source: beans.proto

/* eslint-disable */
import { BinaryReader, BinaryWriter } from "@bufbuild/protobuf/wire";

export const protobufPackage = "io.github.sibmaks.spring.jfr.dto.protobuf.beans";

export interface BeanDefinition {
  scope: number;
  className: number;
  name: number;
  primary: number;
  dependencies: number[];
  stereotype: number;
  generated: boolean;
}

export interface BeanInitialized {
  contextId: number;
  beanName: number;
  preInitializedAt?: number | undefined;
  postInitializedAt?: number | undefined;
  duration: number;
}

export interface BeanDefinitionList {
  beanDefinitions: BeanDefinition[];
}

export interface BeansReport {
  beanDefinitions: { [key: number]: BeanDefinitionList };
  beans: BeanInitialized[];
}

export interface BeansReport_BeanDefinitionsEntry {
  key: number;
  value: BeanDefinitionList | undefined;
}

function createBaseBeanDefinition(): BeanDefinition {
  return { scope: 0, className: 0, name: 0, primary: 0, dependencies: [], stereotype: 0, generated: false };
}

export const BeanDefinition: MessageFns<BeanDefinition> = {
  encode(message: BeanDefinition, writer: BinaryWriter = new BinaryWriter()): BinaryWriter {
    if (message.scope !== 0) {
      writer.uint32(8).int32(message.scope);
    }
    if (message.className !== 0) {
      writer.uint32(16).int32(message.className);
    }
    if (message.name !== 0) {
      writer.uint32(24).int32(message.name);
    }
    if (message.primary !== 0) {
      writer.uint32(32).int32(message.primary);
    }
    writer.uint32(42).fork();
    for (const v of message.dependencies) {
      writer.int32(v);
    }
    writer.join();
    if (message.stereotype !== 0) {
      writer.uint32(48).int32(message.stereotype);
    }
    if (message.generated !== false) {
      writer.uint32(56).bool(message.generated);
    }
    return writer;
  },

  decode(input: BinaryReader | Uint8Array, length?: number): BeanDefinition {
    const reader = input instanceof BinaryReader ? input : new BinaryReader(input);
    let end = length === undefined ? reader.len : reader.pos + length;
    const message = createBaseBeanDefinition();
    while (reader.pos < end) {
      const tag = reader.uint32();
      switch (tag >>> 3) {
        case 1: {
          if (tag !== 8) {
            break;
          }

          message.scope = reader.int32();
          continue;
        }
        case 2: {
          if (tag !== 16) {
            break;
          }

          message.className = reader.int32();
          continue;
        }
        case 3: {
          if (tag !== 24) {
            break;
          }

          message.name = reader.int32();
          continue;
        }
        case 4: {
          if (tag !== 32) {
            break;
          }

          message.primary = reader.int32();
          continue;
        }
        case 5: {
          if (tag === 40) {
            message.dependencies.push(reader.int32());

            continue;
          }

          if (tag === 42) {
            const end2 = reader.uint32() + reader.pos;
            while (reader.pos < end2) {
              message.dependencies.push(reader.int32());
            }

            continue;
          }

          break;
        }
        case 6: {
          if (tag !== 48) {
            break;
          }

          message.stereotype = reader.int32();
          continue;
        }
        case 7: {
          if (tag !== 56) {
            break;
          }

          message.generated = reader.bool();
          continue;
        }
      }
      if ((tag & 7) === 4 || tag === 0) {
        break;
      }
      reader.skip(tag & 7);
    }
    return message;
  },

  fromJSON(object: any): BeanDefinition {
    return {
      scope: isSet(object.scope) ? globalThis.Number(object.scope) : 0,
      className: isSet(object.className) ? globalThis.Number(object.className) : 0,
      name: isSet(object.name) ? globalThis.Number(object.name) : 0,
      primary: isSet(object.primary) ? globalThis.Number(object.primary) : 0,
      dependencies: globalThis.Array.isArray(object?.dependencies)
        ? object.dependencies.map((e: any) => globalThis.Number(e))
        : [],
      stereotype: isSet(object.stereotype) ? globalThis.Number(object.stereotype) : 0,
      generated: isSet(object.generated) ? globalThis.Boolean(object.generated) : false,
    };
  },

  toJSON(message: BeanDefinition): unknown {
    const obj: any = {};
    if (message.scope !== 0) {
      obj.scope = Math.round(message.scope);
    }
    if (message.className !== 0) {
      obj.className = Math.round(message.className);
    }
    if (message.name !== 0) {
      obj.name = Math.round(message.name);
    }
    if (message.primary !== 0) {
      obj.primary = Math.round(message.primary);
    }
    if (message.dependencies?.length) {
      obj.dependencies = message.dependencies.map((e) => Math.round(e));
    }
    if (message.stereotype !== 0) {
      obj.stereotype = Math.round(message.stereotype);
    }
    if (message.generated !== false) {
      obj.generated = message.generated;
    }
    return obj;
  },

  create<I extends Exact<DeepPartial<BeanDefinition>, I>>(base?: I): BeanDefinition {
    return BeanDefinition.fromPartial(base ?? ({} as any));
  },
  fromPartial<I extends Exact<DeepPartial<BeanDefinition>, I>>(object: I): BeanDefinition {
    const message = createBaseBeanDefinition();
    message.scope = object.scope ?? 0;
    message.className = object.className ?? 0;
    message.name = object.name ?? 0;
    message.primary = object.primary ?? 0;
    message.dependencies = object.dependencies?.map((e) => e) || [];
    message.stereotype = object.stereotype ?? 0;
    message.generated = object.generated ?? false;
    return message;
  },
};

function createBaseBeanInitialized(): BeanInitialized {
  return { contextId: 0, beanName: 0, preInitializedAt: undefined, postInitializedAt: undefined, duration: 0 };
}

export const BeanInitialized: MessageFns<BeanInitialized> = {
  encode(message: BeanInitialized, writer: BinaryWriter = new BinaryWriter()): BinaryWriter {
    if (message.contextId !== 0) {
      writer.uint32(8).int32(message.contextId);
    }
    if (message.beanName !== 0) {
      writer.uint32(16).int32(message.beanName);
    }
    if (message.preInitializedAt !== undefined) {
      writer.uint32(24).int64(message.preInitializedAt);
    }
    if (message.postInitializedAt !== undefined) {
      writer.uint32(32).int64(message.postInitializedAt);
    }
    if (message.duration !== 0) {
      writer.uint32(41).double(message.duration);
    }
    return writer;
  },

  decode(input: BinaryReader | Uint8Array, length?: number): BeanInitialized {
    const reader = input instanceof BinaryReader ? input : new BinaryReader(input);
    let end = length === undefined ? reader.len : reader.pos + length;
    const message = createBaseBeanInitialized();
    while (reader.pos < end) {
      const tag = reader.uint32();
      switch (tag >>> 3) {
        case 1: {
          if (tag !== 8) {
            break;
          }

          message.contextId = reader.int32();
          continue;
        }
        case 2: {
          if (tag !== 16) {
            break;
          }

          message.beanName = reader.int32();
          continue;
        }
        case 3: {
          if (tag !== 24) {
            break;
          }

          message.preInitializedAt = longToNumber(reader.int64());
          continue;
        }
        case 4: {
          if (tag !== 32) {
            break;
          }

          message.postInitializedAt = longToNumber(reader.int64());
          continue;
        }
        case 5: {
          if (tag !== 41) {
            break;
          }

          message.duration = reader.double();
          continue;
        }
      }
      if ((tag & 7) === 4 || tag === 0) {
        break;
      }
      reader.skip(tag & 7);
    }
    return message;
  },

  fromJSON(object: any): BeanInitialized {
    return {
      contextId: isSet(object.contextId) ? globalThis.Number(object.contextId) : 0,
      beanName: isSet(object.beanName) ? globalThis.Number(object.beanName) : 0,
      preInitializedAt: isSet(object.preInitializedAt) ? globalThis.Number(object.preInitializedAt) : undefined,
      postInitializedAt: isSet(object.postInitializedAt) ? globalThis.Number(object.postInitializedAt) : undefined,
      duration: isSet(object.duration) ? globalThis.Number(object.duration) : 0,
    };
  },

  toJSON(message: BeanInitialized): unknown {
    const obj: any = {};
    if (message.contextId !== 0) {
      obj.contextId = Math.round(message.contextId);
    }
    if (message.beanName !== 0) {
      obj.beanName = Math.round(message.beanName);
    }
    if (message.preInitializedAt !== undefined) {
      obj.preInitializedAt = Math.round(message.preInitializedAt);
    }
    if (message.postInitializedAt !== undefined) {
      obj.postInitializedAt = Math.round(message.postInitializedAt);
    }
    if (message.duration !== 0) {
      obj.duration = message.duration;
    }
    return obj;
  },

  create<I extends Exact<DeepPartial<BeanInitialized>, I>>(base?: I): BeanInitialized {
    return BeanInitialized.fromPartial(base ?? ({} as any));
  },
  fromPartial<I extends Exact<DeepPartial<BeanInitialized>, I>>(object: I): BeanInitialized {
    const message = createBaseBeanInitialized();
    message.contextId = object.contextId ?? 0;
    message.beanName = object.beanName ?? 0;
    message.preInitializedAt = object.preInitializedAt ?? undefined;
    message.postInitializedAt = object.postInitializedAt ?? undefined;
    message.duration = object.duration ?? 0;
    return message;
  },
};

function createBaseBeanDefinitionList(): BeanDefinitionList {
  return { beanDefinitions: [] };
}

export const BeanDefinitionList: MessageFns<BeanDefinitionList> = {
  encode(message: BeanDefinitionList, writer: BinaryWriter = new BinaryWriter()): BinaryWriter {
    for (const v of message.beanDefinitions) {
      BeanDefinition.encode(v!, writer.uint32(10).fork()).join();
    }
    return writer;
  },

  decode(input: BinaryReader | Uint8Array, length?: number): BeanDefinitionList {
    const reader = input instanceof BinaryReader ? input : new BinaryReader(input);
    let end = length === undefined ? reader.len : reader.pos + length;
    const message = createBaseBeanDefinitionList();
    while (reader.pos < end) {
      const tag = reader.uint32();
      switch (tag >>> 3) {
        case 1: {
          if (tag !== 10) {
            break;
          }

          message.beanDefinitions.push(BeanDefinition.decode(reader, reader.uint32()));
          continue;
        }
      }
      if ((tag & 7) === 4 || tag === 0) {
        break;
      }
      reader.skip(tag & 7);
    }
    return message;
  },

  fromJSON(object: any): BeanDefinitionList {
    return {
      beanDefinitions: globalThis.Array.isArray(object?.beanDefinitions)
        ? object.beanDefinitions.map((e: any) => BeanDefinition.fromJSON(e))
        : [],
    };
  },

  toJSON(message: BeanDefinitionList): unknown {
    const obj: any = {};
    if (message.beanDefinitions?.length) {
      obj.beanDefinitions = message.beanDefinitions.map((e) => BeanDefinition.toJSON(e));
    }
    return obj;
  },

  create<I extends Exact<DeepPartial<BeanDefinitionList>, I>>(base?: I): BeanDefinitionList {
    return BeanDefinitionList.fromPartial(base ?? ({} as any));
  },
  fromPartial<I extends Exact<DeepPartial<BeanDefinitionList>, I>>(object: I): BeanDefinitionList {
    const message = createBaseBeanDefinitionList();
    message.beanDefinitions = object.beanDefinitions?.map((e) => BeanDefinition.fromPartial(e)) || [];
    return message;
  },
};

function createBaseBeansReport(): BeansReport {
  return { beanDefinitions: {}, beans: [] };
}

export const BeansReport: MessageFns<BeansReport> = {
  encode(message: BeansReport, writer: BinaryWriter = new BinaryWriter()): BinaryWriter {
    Object.entries(message.beanDefinitions).forEach(([key, value]) => {
      BeansReport_BeanDefinitionsEntry.encode({ key: key as any, value }, writer.uint32(10).fork()).join();
    });
    for (const v of message.beans) {
      BeanInitialized.encode(v!, writer.uint32(18).fork()).join();
    }
    return writer;
  },

  decode(input: BinaryReader | Uint8Array, length?: number): BeansReport {
    const reader = input instanceof BinaryReader ? input : new BinaryReader(input);
    let end = length === undefined ? reader.len : reader.pos + length;
    const message = createBaseBeansReport();
    while (reader.pos < end) {
      const tag = reader.uint32();
      switch (tag >>> 3) {
        case 1: {
          if (tag !== 10) {
            break;
          }

          const entry1 = BeansReport_BeanDefinitionsEntry.decode(reader, reader.uint32());
          if (entry1.value !== undefined) {
            message.beanDefinitions[entry1.key] = entry1.value;
          }
          continue;
        }
        case 2: {
          if (tag !== 18) {
            break;
          }

          message.beans.push(BeanInitialized.decode(reader, reader.uint32()));
          continue;
        }
      }
      if ((tag & 7) === 4 || tag === 0) {
        break;
      }
      reader.skip(tag & 7);
    }
    return message;
  },

  fromJSON(object: any): BeansReport {
    return {
      beanDefinitions: isObject(object.beanDefinitions)
        ? Object.entries(object.beanDefinitions).reduce<{ [key: number]: BeanDefinitionList }>((acc, [key, value]) => {
          acc[globalThis.Number(key)] = BeanDefinitionList.fromJSON(value);
          return acc;
        }, {})
        : {},
      beans: globalThis.Array.isArray(object?.beans) ? object.beans.map((e: any) => BeanInitialized.fromJSON(e)) : [],
    };
  },

  toJSON(message: BeansReport): unknown {
    const obj: any = {};
    if (message.beanDefinitions) {
      const entries = Object.entries(message.beanDefinitions);
      if (entries.length > 0) {
        obj.beanDefinitions = {};
        entries.forEach(([k, v]) => {
          obj.beanDefinitions[k] = BeanDefinitionList.toJSON(v);
        });
      }
    }
    if (message.beans?.length) {
      obj.beans = message.beans.map((e) => BeanInitialized.toJSON(e));
    }
    return obj;
  },

  create<I extends Exact<DeepPartial<BeansReport>, I>>(base?: I): BeansReport {
    return BeansReport.fromPartial(base ?? ({} as any));
  },
  fromPartial<I extends Exact<DeepPartial<BeansReport>, I>>(object: I): BeansReport {
    const message = createBaseBeansReport();
    message.beanDefinitions = Object.entries(object.beanDefinitions ?? {}).reduce<
      { [key: number]: BeanDefinitionList }
    >((acc, [key, value]) => {
      if (value !== undefined) {
        acc[globalThis.Number(key)] = BeanDefinitionList.fromPartial(value);
      }
      return acc;
    }, {});
    message.beans = object.beans?.map((e) => BeanInitialized.fromPartial(e)) || [];
    return message;
  },
};

function createBaseBeansReport_BeanDefinitionsEntry(): BeansReport_BeanDefinitionsEntry {
  return { key: 0, value: undefined };
}

export const BeansReport_BeanDefinitionsEntry: MessageFns<BeansReport_BeanDefinitionsEntry> = {
  encode(message: BeansReport_BeanDefinitionsEntry, writer: BinaryWriter = new BinaryWriter()): BinaryWriter {
    if (message.key !== 0) {
      writer.uint32(8).int32(message.key);
    }
    if (message.value !== undefined) {
      BeanDefinitionList.encode(message.value, writer.uint32(18).fork()).join();
    }
    return writer;
  },

  decode(input: BinaryReader | Uint8Array, length?: number): BeansReport_BeanDefinitionsEntry {
    const reader = input instanceof BinaryReader ? input : new BinaryReader(input);
    let end = length === undefined ? reader.len : reader.pos + length;
    const message = createBaseBeansReport_BeanDefinitionsEntry();
    while (reader.pos < end) {
      const tag = reader.uint32();
      switch (tag >>> 3) {
        case 1: {
          if (tag !== 8) {
            break;
          }

          message.key = reader.int32();
          continue;
        }
        case 2: {
          if (tag !== 18) {
            break;
          }

          message.value = BeanDefinitionList.decode(reader, reader.uint32());
          continue;
        }
      }
      if ((tag & 7) === 4 || tag === 0) {
        break;
      }
      reader.skip(tag & 7);
    }
    return message;
  },

  fromJSON(object: any): BeansReport_BeanDefinitionsEntry {
    return {
      key: isSet(object.key) ? globalThis.Number(object.key) : 0,
      value: isSet(object.value) ? BeanDefinitionList.fromJSON(object.value) : undefined,
    };
  },

  toJSON(message: BeansReport_BeanDefinitionsEntry): unknown {
    const obj: any = {};
    if (message.key !== 0) {
      obj.key = Math.round(message.key);
    }
    if (message.value !== undefined) {
      obj.value = BeanDefinitionList.toJSON(message.value);
    }
    return obj;
  },

  create<I extends Exact<DeepPartial<BeansReport_BeanDefinitionsEntry>, I>>(
    base?: I,
  ): BeansReport_BeanDefinitionsEntry {
    return BeansReport_BeanDefinitionsEntry.fromPartial(base ?? ({} as any));
  },
  fromPartial<I extends Exact<DeepPartial<BeansReport_BeanDefinitionsEntry>, I>>(
    object: I,
  ): BeansReport_BeanDefinitionsEntry {
    const message = createBaseBeansReport_BeanDefinitionsEntry();
    message.key = object.key ?? 0;
    message.value = (object.value !== undefined && object.value !== null)
      ? BeanDefinitionList.fromPartial(object.value)
      : undefined;
    return message;
  },
};

type Builtin = Date | Function | Uint8Array | string | number | boolean | undefined;

export type DeepPartial<T> = T extends Builtin ? T
  : T extends globalThis.Array<infer U> ? globalThis.Array<DeepPartial<U>>
  : T extends ReadonlyArray<infer U> ? ReadonlyArray<DeepPartial<U>>
  : T extends {} ? { [K in keyof T]?: DeepPartial<T[K]> }
  : Partial<T>;

type KeysOfUnion<T> = T extends T ? keyof T : never;
export type Exact<P, I extends P> = P extends Builtin ? P
  : P & { [K in keyof P]: Exact<P[K], I[K]> } & { [K in Exclude<keyof I, KeysOfUnion<P>>]: never };

function longToNumber(int64: { toString(): string }): number {
  const num = globalThis.Number(int64.toString());
  if (num > globalThis.Number.MAX_SAFE_INTEGER) {
    throw new globalThis.Error("Value is larger than Number.MAX_SAFE_INTEGER");
  }
  if (num < globalThis.Number.MIN_SAFE_INTEGER) {
    throw new globalThis.Error("Value is smaller than Number.MIN_SAFE_INTEGER");
  }
  return num;
}

function isObject(value: any): boolean {
  return typeof value === "object" && value !== null;
}

function isSet(value: any): boolean {
  return value !== null && value !== undefined;
}

export interface MessageFns<T> {
  encode(message: T, writer?: BinaryWriter): BinaryWriter;
  decode(input: BinaryReader | Uint8Array, length?: number): T;
  fromJSON(object: any): T;
  toJSON(message: T): unknown;
  create<I extends Exact<DeepPartial<T>, I>>(base?: I): T;
  fromPartial<I extends Exact<DeepPartial<T>, I>>(object: I): T;
}
