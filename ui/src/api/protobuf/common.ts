// Code generated by protoc-gen-ts_proto. DO NOT EDIT.
// versions:
//   protoc-gen-ts_proto  v2.6.1
//   protoc               v5.29.3
// source: common.proto

/* eslint-disable */
import { BinaryReader, BinaryWriter } from "@bufbuild/protobuf/wire";
import { BeansReport } from "./beans";
import { CallsReport } from "./calls";
import { ConnectionsReport } from "./connections";
import { KafkaConsumersReport } from "./kafka.consumer";

export const protobufPackage = "io.github.sibmaks.spring.jfr.dto.protobuf.common";

export interface CommonDto {
  stringConstants: string[];
}

export interface RootReport {
  common: CommonDto | undefined;
  beans: BeansReport | undefined;
  calls: CallsReport | undefined;
  connections: ConnectionsReport | undefined;
  kafkaConsumers: KafkaConsumersReport | undefined;
}

function createBaseCommonDto(): CommonDto {
  return { stringConstants: [] };
}

export const CommonDto: MessageFns<CommonDto> = {
  encode(message: CommonDto, writer: BinaryWriter = new BinaryWriter()): BinaryWriter {
    for (const v of message.stringConstants) {
      writer.uint32(10).string(v!);
    }
    return writer;
  },

  decode(input: BinaryReader | Uint8Array, length?: number): CommonDto {
    const reader = input instanceof BinaryReader ? input : new BinaryReader(input);
    let end = length === undefined ? reader.len : reader.pos + length;
    const message = createBaseCommonDto();
    while (reader.pos < end) {
      const tag = reader.uint32();
      switch (tag >>> 3) {
        case 1: {
          if (tag !== 10) {
            break;
          }

          message.stringConstants.push(reader.string());
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

  fromJSON(object: any): CommonDto {
    return {
      stringConstants: globalThis.Array.isArray(object?.stringConstants)
        ? object.stringConstants.map((e: any) => globalThis.String(e))
        : [],
    };
  },

  toJSON(message: CommonDto): unknown {
    const obj: any = {};
    if (message.stringConstants?.length) {
      obj.stringConstants = message.stringConstants;
    }
    return obj;
  },

  create<I extends Exact<DeepPartial<CommonDto>, I>>(base?: I): CommonDto {
    return CommonDto.fromPartial(base ?? ({} as any));
  },
  fromPartial<I extends Exact<DeepPartial<CommonDto>, I>>(object: I): CommonDto {
    const message = createBaseCommonDto();
    message.stringConstants = object.stringConstants?.map((e) => e) || [];
    return message;
  },
};

function createBaseRootReport(): RootReport {
  return { common: undefined, beans: undefined, calls: undefined, connections: undefined, kafkaConsumers: undefined };
}

export const RootReport: MessageFns<RootReport> = {
  encode(message: RootReport, writer: BinaryWriter = new BinaryWriter()): BinaryWriter {
    if (message.common !== undefined) {
      CommonDto.encode(message.common, writer.uint32(10).fork()).join();
    }
    if (message.beans !== undefined) {
      BeansReport.encode(message.beans, writer.uint32(18).fork()).join();
    }
    if (message.calls !== undefined) {
      CallsReport.encode(message.calls, writer.uint32(26).fork()).join();
    }
    if (message.connections !== undefined) {
      ConnectionsReport.encode(message.connections, writer.uint32(34).fork()).join();
    }
    if (message.kafkaConsumers !== undefined) {
      KafkaConsumersReport.encode(message.kafkaConsumers, writer.uint32(42).fork()).join();
    }
    return writer;
  },

  decode(input: BinaryReader | Uint8Array, length?: number): RootReport {
    const reader = input instanceof BinaryReader ? input : new BinaryReader(input);
    let end = length === undefined ? reader.len : reader.pos + length;
    const message = createBaseRootReport();
    while (reader.pos < end) {
      const tag = reader.uint32();
      switch (tag >>> 3) {
        case 1: {
          if (tag !== 10) {
            break;
          }

          message.common = CommonDto.decode(reader, reader.uint32());
          continue;
        }
        case 2: {
          if (tag !== 18) {
            break;
          }

          message.beans = BeansReport.decode(reader, reader.uint32());
          continue;
        }
        case 3: {
          if (tag !== 26) {
            break;
          }

          message.calls = CallsReport.decode(reader, reader.uint32());
          continue;
        }
        case 4: {
          if (tag !== 34) {
            break;
          }

          message.connections = ConnectionsReport.decode(reader, reader.uint32());
          continue;
        }
        case 5: {
          if (tag !== 42) {
            break;
          }

          message.kafkaConsumers = KafkaConsumersReport.decode(reader, reader.uint32());
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

  fromJSON(object: any): RootReport {
    return {
      common: isSet(object.common) ? CommonDto.fromJSON(object.common) : undefined,
      beans: isSet(object.beans) ? BeansReport.fromJSON(object.beans) : undefined,
      calls: isSet(object.calls) ? CallsReport.fromJSON(object.calls) : undefined,
      connections: isSet(object.connections) ? ConnectionsReport.fromJSON(object.connections) : undefined,
      kafkaConsumers: isSet(object.kafkaConsumers) ? KafkaConsumersReport.fromJSON(object.kafkaConsumers) : undefined,
    };
  },

  toJSON(message: RootReport): unknown {
    const obj: any = {};
    if (message.common !== undefined) {
      obj.common = CommonDto.toJSON(message.common);
    }
    if (message.beans !== undefined) {
      obj.beans = BeansReport.toJSON(message.beans);
    }
    if (message.calls !== undefined) {
      obj.calls = CallsReport.toJSON(message.calls);
    }
    if (message.connections !== undefined) {
      obj.connections = ConnectionsReport.toJSON(message.connections);
    }
    if (message.kafkaConsumers !== undefined) {
      obj.kafkaConsumers = KafkaConsumersReport.toJSON(message.kafkaConsumers);
    }
    return obj;
  },

  create<I extends Exact<DeepPartial<RootReport>, I>>(base?: I): RootReport {
    return RootReport.fromPartial(base ?? ({} as any));
  },
  fromPartial<I extends Exact<DeepPartial<RootReport>, I>>(object: I): RootReport {
    const message = createBaseRootReport();
    message.common = (object.common !== undefined && object.common !== null)
      ? CommonDto.fromPartial(object.common)
      : undefined;
    message.beans = (object.beans !== undefined && object.beans !== null)
      ? BeansReport.fromPartial(object.beans)
      : undefined;
    message.calls = (object.calls !== undefined && object.calls !== null)
      ? CallsReport.fromPartial(object.calls)
      : undefined;
    message.connections = (object.connections !== undefined && object.connections !== null)
      ? ConnectionsReport.fromPartial(object.connections)
      : undefined;
    message.kafkaConsumers = (object.kafkaConsumers !== undefined && object.kafkaConsumers !== null)
      ? KafkaConsumersReport.fromPartial(object.kafkaConsumers)
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
